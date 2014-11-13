package no.uninett.adc.neo.blindvei.converter;

import no.uninett.adc.neo.domain.ChangeStateCode;

import org.springframework.core.convert.converter.Converter;

public class ChangeStateCodeToInteger implements Converter<ChangeStateCode, Integer> {

	@Override
	public Integer convert(final ChangeStateCode code) {
		switch (code) {
		case PENDING:
			return Integer.valueOf(0);
		case SUCCESS:
			return Integer.valueOf(1);
		case ERROR:
			return Integer.valueOf(2);
		case WARN:
			return Integer.valueOf(3);
		case WAITING:
			return Integer.valueOf(4);
		case DELIVERED:
			return Integer.valueOf(5);
		}
		throw new RuntimeException("Unknown ChangeStateCode " + code);
	}

}
