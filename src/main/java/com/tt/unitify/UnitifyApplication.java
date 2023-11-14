package com.tt.unitify;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tt.unitify.config.Firebase.ServiceAccount;
import com.tt.unitify.modules.pdf.PdfGenerator;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDto;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MiscellaneousExpenses;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MonthlyReportDto;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollData;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollReportDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@SpringBootApplication
public class UnitifyApplication {

	@Autowired
	PdfGenerator pdfGenerator;


	public static void main(String[] args) throws IOException {
		log.info("UnitifyApplication Ready google cloud test");
		ServiceAccount account = new ServiceAccount();
		account.setType("service_account");
		account.setProject_id("unitify-dc8f2");
		account.setPrivate_key_id("0abd48b82a3ee4f070fcbf86e45c29a288c07964");
		account.setPrivate_key("-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdTcVykwd+ctsk\nb+pkgxaYc5ARvR/kRnTVgEw6KMzulv6Lbrb13dDZYXHBaZrROne6xrwI2kuTkHVc\nEO+cS1g6/U00UIMK7svJn7clClzbhgJzs+q23LiQUpZM5SULtXNRDp5QMRpSNF+Q\nsmLhbIL3HPgp3zUPYS3Gsq6tBkRjXkpCkwpdUeb2Z3u7TD9MT3wxAp5qLr8ePxGe\nuA1KhQVr1E9zT4GXc0rR7xHLNFCVGI4lkwkHdx5sAqc6riQ64w5Q2caN5xL9jIj/\nBwQFD37LvK4kmzL+68xqTVvT1JZZyM5wVfT3a1Bg+U4Gf+ZHq4wGBAXKVq5OHdln\n78Ft7oXhAgMBAAECggEAEGA++e/h4Swb3EM6Z5RYudjuBVVnCItn26zWQrJqAzyn\nleRvb6Z+o41yxkE0ONaZYvxlfuvFimszjI4cI8KZliqLQF2msXCtY87DcOZknFtA\neMMg4ECXhjl9eYExcMfxurKG5gT2nzA3SAe00OWht2VwqwtC/tg4Z8MaTtukKvlF\nf/XT7c9yzX9F0ijVMlQTQusLGTNDcaCR0l48++avAF4ImrLuGNPVtvZmtS83/ADg\n+Ev+ZB6aOmnAEoebII2NDcG/u6Cf0OrcpqJSLgnlXitII5C4iXR0oH5e3rwHhOyu\n5ulpk1PhEZAywJ6DkTKLG/r6nluD9eSGubXl5qBPsQKBgQDMJIgoZDXogYDt6Xjx\nxx8zs7SzbIVnw0fNP9oDXj/rSp01hoQXVc5yBk3eZydkz1jnpmCXot1vPfgZOiD9\nZjXHoFT+kzJDWwgrYhnQSCtzG+fG9e50aQp5HayQIp4vzQZo9/+5BdYV6AMWPlIR\nzaAPNHXFW480vxD0kHhnMGI8GwKBgQDFQ0ofuQuPtM9qfsb4QHNc46C9p7j56sy2\nei5cMN9iNSNab3BIJUE+P/5XukdchIcuSEdaubaDXbhQNJgLwkpob2zpTBcwiKbG\n6yk07NDoHRJAoMJnQLFrU+uDaI+rGq9XeiBIxgC5/j4MU2nCE6feIYYzEyU+K9Lv\n0HBQHWVtswKBgGxbD7JwznRlGOwvOrUKvjMRNd7uwTi8XIZDWBLlmgPoDN32UnKY\nZwX69qYQFsbZkBUVKLwUBHkhaWjqg8w8zlrxbWva8Zjy3rk4Tv9Q708gBryCmVlM\nz/RNneS7mz9AEiDxUdkBkgr6pXjPB3zPqh+n9sMUwER9DiDh9ia/dcT1AoGBAIIp\ngIOmKobMnAB9XvT4Micc3DfIsDnYQPQ3ctfKN9wpjAZkegZg7sgQmddxRx05JanY\nb5zh5hiQgsoJqjo4HbkDrS3T04lm4gVnrm0Jxl8ir6SNbzmKaoeTfFfdas9QcFqc\nbykV3ezcYjzn3WT+a0obfN2+yFhZunobY8C5ZPAZAoGAAMIOtJidG+EAvslZZXWa\n24DDl5v7w4D3iGsxYb1fLum6mn7YMPxJZrB4/jepS9859a3zBRIgpyrRdetKpmay\nb+c4jzouIOdm0oqEOcZ2s33GJbhRsTY7dSLUn7V6rRbWheC18q1ffA0552ZlMbMx\nu2OsHlvVgomDGjrad6T4l2U=\n-----END PRIVATE KEY-----\n");
		account.setClient_email("firebase-adminsdk-pg5o5@unitify-dc8f2.iam.gserviceaccount.com");
		account.setClient_id("109705264032572027262");
		account.setAuth_uri("https://accounts.google.com/o/oauth2/auth");
		account.setToken_uri("https://oauth2.googleapis.com/token");
		account.setAuth_provider_x509_cert_url("https://www.googleapis.com/oauth2/v1/certs");
		account.setClient_x509_cert_url("https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-pg5o5%40unitify-dc8f2.iam.gserviceaccount.com");
		account.setUniverse_domain("googleapis.com");


		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(account);
		log.info("json-cloud: {}", json);
		// Crear un archivo temporal
		File tempFile = File.createTempFile("service_account", ".json");

		// Escribir el JSON en el archivo temporal
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(json.getBytes());
		}
		try (FileInputStream serviceAccount = new FileInputStream(tempFile)) {
			tempFile.delete();
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options);


		} catch (Exception e) {
			e.printStackTrace();
		}
		SpringApplication.run(UnitifyApplication.class, args);
	}

}
