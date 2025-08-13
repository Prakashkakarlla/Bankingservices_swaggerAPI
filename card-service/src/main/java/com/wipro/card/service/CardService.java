package com.wipro.card.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.wipro.card.entity.Card;
import com.wipro.card.event.NotificationEvent;
import com.wipro.card.exception.ResourceNotFoundException;
import com.wipro.card.repository.CardRepository;

import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Card issueCard(Card card) {
        if (card.getAccountId() == null || card.getType() == null) {
            throw new IllegalArgumentException("Account ID and card type are required");
        }
        // Verify account exists
        String url = "http://account-service/api/accounts/" + card.getAccountId() + "/balance";
        restTemplate.getForObject(url, Double.class);
        card.setCardNumber(UUID.randomUUID().toString());
        card.setStatus("ACTIVE");
        Card savedCard = cardRepository.save(card);
        eventPublisher.publishEvent(new NotificationEvent(this, "CARD_ISSUED", card.getAccountId(), "Card issued: " + card.getType(), "user@example.com"));
        return savedCard;
    }

    @Transactional
    public Card blockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        if (!card.getStatus().equals("ACTIVE")) {
            throw new IllegalArgumentException("Only active cards can be blocked");
        }
        card.setStatus("BLOCKED");
        Card blockedCard = cardRepository.save(card);
        eventPublisher.publishEvent(new NotificationEvent(this, "CARD_BLOCKED", card.getAccountId(), "Card blocked", "user@example.com"));
        return blockedCard;
    }

    @Transactional
    public Card closeCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        if (card.getStatus().equals("CLOSED")) {
            throw new IllegalArgumentException("Card is already closed");
        }
        card.setStatus("CLOSED");
        Card closedCard = cardRepository.save(card);
        eventPublisher.publishEvent(new NotificationEvent(this, "CARD_CLOSED", card.getAccountId(), "Card closed", "user@example.com"));
        return closedCard;
    }
}
