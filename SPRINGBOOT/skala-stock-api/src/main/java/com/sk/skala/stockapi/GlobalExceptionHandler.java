package com.sk.skala.stockapi;

import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.sk.skala.stockapi.config.Error;
import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.exception.ParameterException;
import com.sk.skala.stockapi.exception.ResponseException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public @ResponseBody Response takeException(Exception e) {
		Response response = new Response();
		if (isValidationException(e)) {
			response.setError(Error.PARAMETER_MISSED.getCode(), buildValidationMessage(e));
			log.error("GlobalExceptionHandler.ValidationException: {}", e.getMessage());
			return response;
		}
		response.setError(Error.SYSTEM_ERROR.getCode(), e.getMessage());
		log.error("GlobalExceptionHandler.Exception: {}", e.getMessage());
		return response;
	}

	@ExceptionHandler(value = NullPointerException.class)
	public @ResponseBody Response takeNullPointerException(Exception e) {
		Response response = new Response();
		response.setError(Error.SYSTEM_ERROR.getCode(), e.getMessage());
		log.error("GlobalExceptionHandler.NullPointerException: {}", e.getMessage());
		e.printStackTrace();
		return response;
	}

	@ExceptionHandler(value = SecurityException.class)
	public @ResponseBody Response takeSecurityException(SecurityException e) {
		Response response = new Response();
		response.setError(Error.NOT_AUTHENTICATED.getCode(), e.getMessage());
		log.error("GlobalExceptionHandler.SecurityException: {}", e.getMessage());
		return response;
	}

	@ExceptionHandler(value = ParameterException.class)
	public @ResponseBody Response takeParameterException(ParameterException e) {
		Response response = new Response();
		response.setError(e.getCode(), e.getMessage());
		return response;
	}

	@ExceptionHandler(value = ResponseException.class)
	public @ResponseBody Response takeResponseException(ResponseException e) {
		Response response = new Response();
		response.setError(e.getCode(), e.getMessage());
		return response;
	}

	private boolean isValidationException(Exception e) {
		return e instanceof MethodArgumentNotValidException
				|| e instanceof BindException
				|| e instanceof ConstraintViolationException
				|| e instanceof HandlerMethodValidationException
				|| e instanceof MissingServletRequestParameterException
				|| e instanceof MethodArgumentTypeMismatchException;
	}

	private String buildValidationMessage(Exception e) {
		if (e instanceof MethodArgumentNotValidException manve) {
			return manve.getBindingResult().getFieldErrors().stream()
					.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
					.collect(Collectors.joining(", "));
		}
		if (e instanceof BindException be) {
			return be.getBindingResult().getFieldErrors().stream()
					.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
					.collect(Collectors.joining(", "));
		}
		if (e instanceof ConstraintViolationException cve) {
			return cve.getConstraintViolations().stream()
					.map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
					.collect(Collectors.joining(", "));
		}
		if (e instanceof HandlerMethodValidationException hmve) {
			return hmve.getAllErrors().stream()
					.map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : error.toString())
					.collect(Collectors.joining(", "));
		}
		if (e instanceof MissingServletRequestParameterException msrpe) {
			return msrpe.getParameterName() + " 파라미터는 필수입니다.";
		}
		if (e instanceof MethodArgumentTypeMismatchException matme) {
			return matme.getName() + " 파라미터 타입이 올바르지 않습니다.";
		}
		return e.getMessage();
	}
}
