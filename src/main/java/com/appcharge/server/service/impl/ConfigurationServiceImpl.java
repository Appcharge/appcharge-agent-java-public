package com.appcharge.server.service.impl;

import com.appcharge.server.service.ConfigurationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

  @Value("${server.port}")
  private String serverPort;

  @Value("${AWARD_PUBLISHER_URL}")
  private String awardPublisherUrl;

  @Value("${ASSET_UPLOAD_GATEWAY_URL}")
  private String assetUploadGatewayUrl;

  @Value("${REPORTING_API_URL}")
  private String reportingApiUrl;

  @Value("${offer.update.publisherToken}")
  private String publisherToken;

  @Value("${KEY}")
  private String encryptionKey;

  @Value("${FACEBOOK_APP_SECRET}")
  private String facebookAppSecret;

  @Value("${APPLE_SECRET_API}")
  private String appleSecretApi;

  @Value("${OFFERS_FILE_PATH}")
  private String offersFilePath;

  @Value("${PLAYER_DATASET_FILE_PATH}")
  private String playerDatasetFilePath;

  @Value("${EVENTS_DATASET_FILE_PATH}")
  private String eventsDatasetFilePath;

}
