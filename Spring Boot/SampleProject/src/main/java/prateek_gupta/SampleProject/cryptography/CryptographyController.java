package prateek_gupta.SampleProject.cryptography;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
import prateek_gupta.SampleProject.utils.Util;

import java.util.Arrays;

@RestController
@RequestMapping("cryptography")
public class CryptographyController {
    private final Logger log = LoggerFactory.getLogger(CryptographyController.class);

    @Autowired
    Cryptography cryptography;


    @PostMapping("/des_encrypt")
    ResponseEntity<ObjectNode> desEncrypt(@RequestParam String plainText) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(plainText)) {
                byte[] encryptedData=cryptography.desEncrypt(plainText);
                response = Util.getSuccessResponse("Encrypted Data : " +
                        Arrays.toString(encryptedData));
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        return response;
    }

    @PostMapping("/des_decrypt")
    ResponseEntity<ObjectNode> desDecrypt(@RequestParam String encryptedText) {
        log.info("Entering Controller : desDecrypt()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("CRYPTOGRAPHY_ENABLED", true);

            if (StringUtils.isNotBlank(encryptedText)) {
                String[] strArray=encryptedText.substring(1, encryptedText.length() - 1)
                        .split(",");
                byte[] bytes=new byte[strArray.length];
                for (int i = 0; i < strArray.length; i++) {
                    bytes[i]=Byte.parseByte(strArray[i].trim());
                }
                String plainText=cryptography.desDecrypt(bytes);
                response = Util.getSuccessResponse("Decrypted Data : " +plainText);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception e) {
            return Util.getErrorResponse(
                    new ServiceException(ServiceException.ExceptionType.UNKNOWN_ERROR));
        }
        log.info("Exiting Controller : desDecrypt()");
        return response;
    }
}
