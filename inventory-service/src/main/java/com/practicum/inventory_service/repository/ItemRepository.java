package com.practicum.inventory_service.repository;

import com.practicum.inventory_service.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    Item save(Item item);
    Item findItemById(String id);
    void deleteById(String id);
}
