package com.kclogix.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import kainos.framework.core.KainosKey;
import kainos.framework.core.context.KainosMessageAccessor;
import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.lang.KainosException;
import kainos.framework.core.servlet.KainosResponseEntity;
import com.kclogix.apps.common.error.service.ErrorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalException {

	@Autowired
	private KainosMessageAccessor message;
	@Autowired
	private ErrorService service;
	
	
    /**
     * System Exception
     * @param ex
     * @return
     */
    @ExceptionHandler( Exception.class )
    private ResponseEntity<?> systemException(Exception ex) {
    	log.error("{}", ex);
    	service.insertErrorLog(ex);
    	KainosException kainosException = new KainosException("A system error has occurred. Please contact the administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
    	kainosException.setType(KainosKey.Status.Error);
    	return KainosResponseEntity.builder().failResponse(kainosException).build().close();
    }
    
    /**
     * Business Exception
     * @param ex
     * @return
     */
    @ExceptionHandler( KainosBusinessException.class )
    private ResponseEntity<?> kainosCustomerException(KainosBusinessException ex) {
//    	log.error("{}", ex);
    	KainosException kainosException = new KainosException(message.getMessageConvert(ex.getMessageId(), ex.getArguments()), HttpStatus.OK);
    	kainosException.setType(message.getMessageType(ex.getMessageId()));
    	return KainosResponseEntity.builder().failResponse(kainosException).build().close();
    }
    
    /**
     * parameter valid
     * @param ex
     * @return
     */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	private ResponseEntity<?> validException(MethodArgumentNotValidException ex) {
		log.error("{}", ex);
		String validMessage = ex.getAllErrors().get(0).getDefaultMessage();
		KainosException kainosException = new KainosException(validMessage, HttpStatus.BAD_REQUEST);
    	kainosException.setType(KainosKey.Status.Error);
    	return KainosResponseEntity.builder().failResponse(kainosException).build().close();
	}
	
    /**
     * parameter valid
     * @param ex
     * @return
     */
	@ExceptionHandler({ HandlerMethodValidationException.class })
	private ResponseEntity<?> validException(HandlerMethodValidationException ex) {
		log.error("{}", ex);
		String validMessage = ex.getAllErrors().get(0).getDefaultMessage();
		KainosException kainosException = new KainosException(validMessage, HttpStatus.BAD_REQUEST);
    	kainosException.setType(KainosKey.Status.Error);
    	return KainosResponseEntity.builder().failResponse(kainosException).build().close();
	}
	
	@ExceptionHandler({ NoResourceFoundException.class })
	private void noResourceFoundException(NoResourceFoundException ex) {
	}
	
	/**
	 * parameter is missing"
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({ MissingServletRequestParameterException.class })
	private ResponseEntity<?> validRequestParameterException(MissingServletRequestParameterException ex) {
		log.error("{}", ex);
		KainosException kainosException = new KainosException("[ " + ex.getParameterName() + " ]" + " parameter is missing", HttpStatus.BAD_REQUEST);
    	kainosException.setType(KainosKey.Status.Error);
    	return KainosResponseEntity.builder().failResponse(kainosException).build().close();
	}
	
}
