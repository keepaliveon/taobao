package com.youmeng.taoshelf.repository;

import com.youmeng.taoshelf.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {
    Card findCardById(String id);

    Page<Card> findCardsByUserIsNotNullAndUsedTimeIsNotNull(Pageable pageable);

    Page<Card> findCardsByUserIsNull(Pageable pageable);

    Integer deleteCardsByIdIn(String[] ids);

}
