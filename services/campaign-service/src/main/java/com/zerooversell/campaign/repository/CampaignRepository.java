package com.zerooversell.campaign.repository;

import com.zerooversell.campaign.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {}
