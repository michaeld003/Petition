package com.example.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.example.exception.InvalidBioIdException;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class BioIdValidationService {
    private static final Logger logger = LoggerFactory.getLogger(BioIdValidationService.class);

    private final UserRepository userRepository;

    // Set of valid BioIDs from the requirement
    private static final Set<String> VALID_BIOIDS = new HashSet<>(Set.of(
            "K1YL8VA2HG", "7DMPYAZAP2", "D05HPPQNJ4", "2WYIM3QCK9",
            "DHKFIYHMAZ", "LZK7P0X0LQ", "H5C98XCENC", "6X6I6TSUFG",
            "QTLCWUS8NB", "Y4FC3F9ZGS", "V30EPKZQI2", "O3WJFGR5WE",
            "SEIQTS1H16", "X16V7LFHR2", "TLFDFY7RDG", "PGPVG5RF42",
            "FPALKDEL5T", "2BIB99Z54V", "ABQYUQCQS2", "9JSXWO4LGH",
            "QJXQOUPTH9", "GOYWJVDA8A", "6EBQ28A62V", "30MY51J1CJ",
            "FH6260T08H", "JHDCXB62SA", "O0V55ENOT0", "F3ATSRR5DQ",
            "1K3JTWHA05", "FINNMWJY0G", "CET8NUAE09", "VQKBGSE3EA",
            "E7D6YUPQ6J", "BPX8O0YB5L", "AT66BX2FXM", "1PUQV970LA",
            "CCU1D7QXDT", "TTK74SYYAN", "4HTOAI9YKO", "PD6XPNB80J",
            "BZW5WWDMUY", "340B1EOCMG", "CG1I9SABLL", "49YFTUA96K",
            "V2JX0IC633", "C7IFP4VWIL", "RYU8VSS4N5", "S22A588D75",
            "88V3GKIVSF", "8OLYIE2FRC"
    ));

    public BioIdValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates a manually entered BioID
     * @param bioId The BioID to validate
     * @return true if the BioID is valid and not already in use
     * @throws InvalidBioIdException if the BioID is invalid or already in use
     */
    public boolean validateBioId(String bioId) throws InvalidBioIdException {
        // Check if BioID is in correct format
        if (bioId == null || bioId.length() != 10) {
            throw new InvalidBioIdException("BioID must be 10 characters long");
        }

        // Check if BioID is in the valid set
        if (!VALID_BIOIDS.contains(bioId)) {
            throw new InvalidBioIdException("Invalid BioID provided");
        }

        // Check if BioID is already in use
        if (userRepository.existsByBioId(bioId)) {
            throw new InvalidBioIdException("BioID is already registered");
        }

        return true;
    }

    /**
     * Validates a BioID from a QR code image
     * @param qrCodeFile The QR code image file
     * @return The validated BioID from the QR code
     * @throws InvalidBioIdException if the QR code is invalid or contains an invalid BioID
     */
    public String validateQrCode(MultipartFile qrCodeFile) throws InvalidBioIdException {
        try {
            String bioId = readQRCode(qrCodeFile);

            // Validate the extracted BioID
            if (validateBioId(bioId)) {
                return bioId;
            }

            throw new InvalidBioIdException("Invalid BioID in QR code");
        } catch (IOException e) {
            logger.error("Error reading QR code file", e);
            throw new InvalidBioIdException("Unable to read QR code image");
        } catch (NotFoundException e) {
            logger.error("No QR code found in image", e);
            throw new InvalidBioIdException("No valid QR code found in image");
        } catch (Exception e) {
            logger.error("Error processing QR code", e);
            throw new InvalidBioIdException("Error processing QR code");
        }
    }

    /**
     * Reads a BioID from a QR code image
     * @param qrCodeFile The QR code image file
     * @return The BioID extracted from the QR code
     * @throws IOException if the image cannot be read
     * @throws NotFoundException if no QR code is found
     */
    private String readQRCode(MultipartFile qrCodeFile) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeFile.getInputStream());

        if (bufferedImage == null) {
            throw new IOException("Could not read image file");
        }

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    /**
     * Checks if a given string matches the BioID format
     * @param bioId The string to check
     * @return true if the string matches the BioID format
     */
    public static boolean isValidFormat(String bioId) {
        // BioID should be 10 characters long and contain only alphanumeric characters
        return bioId != null &&
                bioId.length() == 10 &&
                bioId.matches("^[A-Z0-9]+$");
    }

    /**
     * Gets the total number of valid BioIDs
     * @return the number of valid BioIDs
     */
    public static int getValidBioIdCount() {
        return VALID_BIOIDS.size();
    }

    /**
     * Checks if a BioID has been registered
     * @param bioId The BioID to check
     * @return true if the BioID is already registered
     */
    public boolean isBioIdRegistered(String bioId) {
        return userRepository.existsByBioId(bioId);
    }
}