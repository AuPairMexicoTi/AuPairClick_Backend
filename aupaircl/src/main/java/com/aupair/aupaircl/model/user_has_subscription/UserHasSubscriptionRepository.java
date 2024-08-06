package com.aupair.aupaircl.model.user_has_subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserHasSubscriptionRepository extends JpaRepository<UserHasSubscription, UUID>  {

}
