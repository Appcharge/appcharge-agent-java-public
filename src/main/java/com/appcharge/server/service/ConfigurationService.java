package com.appcharge.server.service;

public interface ConfigurationService {

  String getServerPort();

  String getAwardPublisherUrl();

  String getAssetUploadGatewayUrl();

  String getReportingApiUrl();

  String getPublisherToken();

  String getEncryptionKey();

  String getFacebookAppSecret();

  String getAppleSecretApi();

  String getOffersFilePath();

  String getPlayerDatasetFilePath();

  String getEventsDatasetFilePath();
}
