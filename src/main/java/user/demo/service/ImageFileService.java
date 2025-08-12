package user.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageFileService {
    private final Path rootLocation = Paths.get("uploads");

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File trống");
        }
        try {
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(rootLocation);

            // Lấy phần mở rộng file
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // Tạo tên file mới bằng UUID
            String newFileName = UUID.randomUUID().toString() + extension;

            // Lưu file
            Path destinationFile = rootLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            return newFileName; // trả về tên file mới để lưu DB
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + e.getMessage());
        }
    }
}
