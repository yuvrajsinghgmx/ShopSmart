import logging
from django.core.management.base import BaseCommand
from django.utils import timezone
from django.db import transaction
from subscriptions.models import ActiveSubscription

logger = logging.getLogger(__name__)


class Command(BaseCommand):
    """
    A management command to check for expired subscriptions, deactivate them,
    and reset the associated item's position to its default state.
    """
    @transaction.atomic
    def handle(self, *args, **options):
        now = timezone.now()
        self.stdout.write(f"Running subscription expiration check at {now.isoformat()}...")

        expired_subscriptions = ActiveSubscription.objects.filter(end_date__lt=now, is_active=True).select_related('plan', 'content_type')

        if not expired_subscriptions.exists():
            self.stdout.write(self.style.SUCCESS("No expired subscriptions found. Exiting."))
            return

        expired_count = 0
        for subscription in expired_subscriptions:
            item = subscription.content_object
            
            subscription.is_active = False
            subscription.save(update_fields=['is_active'])
            
            if not item:
                self.stdout.write(self.style.WARNING(f"Deactivated Subscription ID {subscription.id} which was linked to a deleted item."))
                expired_count += 1
                continue

            if subscription.plan.position_level:
                if item.position == subscription.plan.position_level:
                    item.position = 1
                    item.save(update_fields=['position'])

                    self.stdout.write(f"Reset position for {item.__class__.__name__} '{item.name}' (ID: {item.id}).")
                else:
                    self.stdout.write(self.style.WARNING(
                        f"Skipping position reset for {item.__class__.__name__} '{item.name}' (ID: {item.id}) "
                        f"as its current position ({item.position}) does not match the expired plan's level ({subscription.plan.position_level})."
                    ))
            
            self.stdout.write(f"Deactivated expired subscription ID {subscription.id} for '{item.name}'.")
            expired_count += 1
        
        self.stdout.write(self.style.SUCCESS(f"Successfully processed and deactivated {expired_count} expired subscription(s)."))