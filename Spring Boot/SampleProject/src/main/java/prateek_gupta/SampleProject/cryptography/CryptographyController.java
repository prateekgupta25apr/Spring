package prateek_gupta.SampleProject.cryptography;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prateek_gupta.SampleProject.prateek_gupta.Cryptography;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.project_utils.Init;

import java.util.HexFormat;

@RestController
@RequestMapping("cryptography")
public class CryptographyController {
    private final Logger log = LoggerFactory.getLogger(
            CryptographyController.class);

    @Autowired
    Cryptography cryptography;


    @PostMapping("/des_encrypt")
    ResponseEntity<ObjectNode> desEncrypt(
            @RequestParam String plain_text,
            @RequestParam(required = false, defaultValue = "") String secret_key) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(plain_text)) {
                byte[] encryptedData=cryptography.desEncrypt(plain_text, secret_key);
                String hex = HexFormat.of().formatHex(encryptedData);
                JSONObject data = new JSONObject();
                data.put("Encrypted Data(Hex)", hex);

                response = Init.getSuccessResponse("Successfully encrypted data",data);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Init.getErrorResponse(exception);
        }
        return response;
    }

    @PostMapping("/des_decrypt")
    ResponseEntity<ObjectNode> desDecrypt(
            @RequestParam String encrypted_text,
            @RequestParam(required = false, defaultValue = "") String secret_key) {
        log.info("Entering Controller : desDecrypt()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(encrypted_text)) {
                byte[] bytes=HexFormat.of().parseHex(encrypted_text);
                String plainText=cryptography.desDecrypt(bytes, secret_key);
                JSONObject data = new JSONObject();
                data.put("Decrypted Data", plainText);

                response = Init.getSuccessResponse("Successfully decrypted data",data);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Init.getErrorResponse(exception);
        } catch (Exception e) {
            return Init.getErrorResponse(
                    new ServiceException(ServiceException.ExceptionType.UNKNOWN_ERROR));
        }
        log.info("Exiting Controller : desDecrypt()");
        return response;
    }

    @PostMapping("/hash_sha_256")
    ResponseEntity<ObjectNode> hashSHA256(@RequestParam String plain_text) {
        log.info("Entering Controller : hashSHA256()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(plain_text)) {
                String hex=cryptography.hashSHA256(plain_text);
                JSONObject data = new JSONObject();
                data.put("Hashed Data(Hex)", hex);
                response = Init.getSuccessResponse("Successfully hashed data",data);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Init.getErrorResponse(exception);
        } catch (Exception e) {
            return Init.getErrorResponse(
                    new ServiceException(ServiceException.ExceptionType.UNKNOWN_ERROR));
        }
        log.info("Exiting Controller : hashSHA256()");
        return response;
    }

    @PostMapping("/hmac_sha_256")
    ResponseEntity<ObjectNode> hMacSHA256(
            @RequestParam String plain_text,
            @RequestParam(required = false, defaultValue = "") String secret_key) {
        log.info("Entering Controller : hMacSHA256()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(plain_text)) {
                String hex=cryptography.hMacSHA256(plain_text, secret_key);
                JSONObject data = new JSONObject();
                data.put("HMac(Hex)", hex);
                response = Init.getSuccessResponse("Successfully generated HMac",data);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Init.getErrorResponse(exception);
        } catch (Exception e) {
            return Init.getErrorResponse(
                    new ServiceException(ServiceException.ExceptionType.UNKNOWN_ERROR));
        }
        log.info("Exiting Controller : hMacSHA256()");
        return response;
    }

    @PostMapping("/aes_encrypt")
    ResponseEntity<ObjectNode> aesEncrypt(
            @RequestParam String plain_text,
            @RequestParam(required = false, defaultValue = "") String secret_key) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(plain_text)) {
                byte[] encryptedData=cryptography.aesEncrypt(plain_text, secret_key);
                String hex = HexFormat.of().formatHex(encryptedData);
                JSONObject data = new JSONObject();
                data.put("Encrypted Data(Hex)", hex);

                response = Init.getSuccessResponse("Successfully encrypted data",data);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Init.getErrorResponse(exception);
        }
        return response;
    }

    @PostMapping("/aes_decrypt")
    ResponseEntity<ObjectNode> aesDecrypt(
            @RequestParam String encrypted_text,
            @RequestParam(required = false, defaultValue = "") String secret_key) {
        log.info("Entering Controller : aesDecrypt()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(encrypted_text)) {
                byte[] bytes=HexFormat.of().parseHex(encrypted_text);
                String plainText=cryptography.aesDecrypt(bytes, secret_key);
                JSONObject data = new JSONObject();
                data.put("Decrypted Data", plainText);

                response = Init.getSuccessResponse("Successfully decrypted data",data);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Init.getErrorResponse(exception);
        } catch (Exception e) {
            return Init.getErrorResponse(
                    new ServiceException(ServiceException.ExceptionType.UNKNOWN_ERROR));
        }
        log.info("Exiting Controller : aesDecrypt()");
        return response;
    }
}
