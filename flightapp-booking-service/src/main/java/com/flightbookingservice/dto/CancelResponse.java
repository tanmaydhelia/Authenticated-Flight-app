package com.flightbookingservice.dto;

import com.flightbookingservice.entity.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelResponse {
	private String pnr;
	private BookingStatus status;
	private String message;
}
