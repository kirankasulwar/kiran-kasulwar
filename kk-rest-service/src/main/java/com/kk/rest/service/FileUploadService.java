package com.kk.rest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

@Service
public class FileUploadService {

	private static final String EXTERNAL_SERVICE_URL = "https://import-service.root.net/batch-api/upload-to-s3";

	/**
	 * Uploads a file to the external service with application-id metadata
	 *
	 * @param applicationId the application ID to include as metadata
	 * @param file the file to upload
	 * @return response from the external service
	 * @throws IOException if file upload fails
	 */
	public Map<String, Object> uploadFileToExternalService(String applicationId, MultipartFile file) throws IOException {
		if (applicationId == null || applicationId.trim().isEmpty()) {
			throw new IllegalArgumentException("Application ID cannot be null or empty");
		}

		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("File cannot be null or empty");
		}

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost uploadFile = new HttpPost(EXTERNAL_SERVICE_URL);

			// Add metadata and file to the request
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("application-id", applicationId);
			builder.addBinaryBody("file", file.getInputStream(), org.apache.hc.core5.http.ContentType.DEFAULT_BINARY, file.getOriginalFilename());

			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);

			// Execute the request and handle the response
			return httpClient.execute(uploadFile, response -> {
				int statusCode = response.getCode();
				String responseBody = EntityUtils.toString(response.getEntity());

				Map<String, Object> result = new HashMap<>();
				result.put("statusCode", statusCode);
				result.put("message", responseBody);
				result.put("success", statusCode >= 200 && statusCode < 300);

				return result;
			});
		}
	}
}

