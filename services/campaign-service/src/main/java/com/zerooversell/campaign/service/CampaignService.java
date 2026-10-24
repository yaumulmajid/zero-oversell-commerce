package com.zerooversell.campaign.service;

import com.zerooversell.campaign.domain.Campaign;
import com.zerooversell.campaign.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Optional;

@Service
public class CampaignService {
    private final CampaignRepository campaigns;
    private final RestClient reservationClient;

    public CampaignService(CampaignRepository campaigns, RestClient reservationClient) {
        this.campaigns = campaigns;
        this.reservationClient = reservationClient;
    }

    public Campaign create(String name, BigDecimal unitPrice, String currency, int quantity) {
        var campaign = new Campaign(UUID.randomUUID(), name, unitPrice, currency, quantity);
        // Provision inventory first; the operation is idempotent for retries after a network timeout.
        reservationClient.post().uri("/internal/v1/inventory")
                .body(new InventoryProvisionRequest(campaign.getId(), quantity, unitPrice, currency))
                .retrieve().toBodilessEntity();
        return campaigns.save(campaign);
    }

    public Optional<Campaign> find(UUID campaignId) { return campaigns.findById(campaignId); }

    public record InventoryProvisionRequest(UUID saleId, int quantity, BigDecimal unitPrice, String currency) {}
}
