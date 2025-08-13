package com.wipro.card.controller;

import com.wipro.card.entity.Card;
import com.wipro.card.service.CardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Issue a new card",
               description = "Creates and issues a new card for the specified account/customer.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card issued successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card details")
    })
    @PostMapping("/issue")
    public ResponseEntity<Card> issueCard(
            @Parameter(description = "Card details", required = true)
            @Valid @RequestBody @NotNull(message = "Card details cannot be null") Card card) {
        return ResponseEntity.ok(cardService.issueCard(card));
    }

    @Operation(summary = "Block a card",
               description = "Blocks the card with the given ID to prevent further transactions.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card blocked successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PostMapping("/{id}/block")
    public ResponseEntity<Card> blockCard(
            @Parameter(description = "Card ID") 
            @PathVariable @NotNull(message = "Card ID cannot be null") Long id) {
        return ResponseEntity.ok(cardService.blockCard(id));
    }

    @Operation(summary = "Close a card",
               description = "Closes the card account with the given ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card closed successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PostMapping("/{id}/close")
    public ResponseEntity<Card> closeCard(
            @Parameter(description = "Card ID") 
            @PathVariable @NotNull(message = "Card ID cannot be null") Long id) {
        return ResponseEntity.ok(cardService.closeCard(id));
    }
}
