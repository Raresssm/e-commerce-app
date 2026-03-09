package com.rares.ecommerce.notification.Notification.Repository;

import com.rares.ecommerce.notification.Notification.Model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, Integer> {
}
