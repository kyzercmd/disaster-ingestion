package com.bd.disaster.service;

import com.bd.disaster.repository.CsvEventRepository;
import com.bd.disaster.validation.StrictEventValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;

class CsvUploadServiceTest {

    @Test
    void testUploadValidAndInvalidRows(@TempDir Path tempDir) throws Exception {
        Path csvPath = tempDir.resolve("events.csv");
        java.nio.file.Files.writeString(csvPath,
            "event_id,hazard_type,start_date,end_date,division,district,upazila," +
            "affected_people,deaths,economic_loss_bdt,economic_loss_usd,duration_days," +
            "severity_category,source_url,notes\n");

        CsvEventRepository repo = new CsvEventRepository(csvPath.toString());
        CsvUploadService svc = new CsvUploadService(
            repo, new StrictEventValidator(), Executors.newFixedThreadPool(2));

        String csvContent =
            "event_id,hazard_type,start_date,end_date,division,district,upazila," +
            "affected_people,deaths,economic_loss_bdt,economic_loss_usd,duration_days," +
            "severity_category,source_url,notes\n" +
            "BD-FLOOD-2024-001,flood,2024-06-01,2024-06-15,Dhaka,Manikganj,Singair," +
            "5000,3,1000000,,,,https://example.com,\n" +
            "BD-BAD-2024-001,tornado,2024-06-01,2024-06-15,Dhaka,Manikganj,Singair," +
            "5000,3,1000000,,,,https://example.com,\n";

        InputStream is = new ByteArrayInputStream(csvContent.getBytes());
        CsvUploadService.UploadResult result = svc.processUpload(is);

        assertEquals(1, result.accepted());
        assertEquals(1, result.rejected());
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void testDuplicateEventIdRejected(@TempDir Path tempDir) throws Exception {
        Path csvPath = tempDir.resolve("events.csv");
        java.nio.file.Files.writeString(csvPath,
            "event_id,hazard_type,start_date,end_date,division,district,upazila," +
            "affected_people,deaths,economic_loss_bdt,economic_loss_usd,duration_days," +
            "severity_category,source_url,notes\n" +
            "BD-FLOOD-2024-001,flood,2024-06-01,2024-06-15,Dhaka,Manikganj,Singair," +
            "5000,3,1000000,,15,,https://example.com,\n");

        CsvEventRepository repo = new CsvEventRepository(csvPath.toString());
        CsvUploadService svc = new CsvUploadService(
            repo, new StrictEventValidator(), Executors.newFixedThreadPool(2));

        String csvContent =
            "event_id,hazard_type,start_date,end_date,division,district,upazila," +
            "affected_people,deaths,economic_loss_bdt,economic_loss_usd,duration_days," +
            "severity_category,source_url,notes\n" +
            "BD-FLOOD-2024-001,flood,2024-06-01,2024-06-15,Dhaka,Manikganj,Singair," +
            "5000,3,1000000,,,,https://example.com,\n";

        InputStream is = new ByteArrayInputStream(csvContent.getBytes());
        CsvUploadService.UploadResult result = svc.processUpload(is);

        assertEquals(0, result.accepted());
        assertEquals(1, result.rejected());
    }
}
