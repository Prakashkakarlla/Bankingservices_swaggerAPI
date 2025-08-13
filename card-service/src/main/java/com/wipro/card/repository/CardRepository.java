package com.wipro.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
	
	
}