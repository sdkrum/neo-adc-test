package no.uninett.adc.neo.blindvei.converter;

import no.uninett.adc.neo.domain.ChangeStateCode;

import org.springframework.core.convert.converter.Converter;

public class IntegerToChangeStateCode implements Converter<Integer, ChangeStateCode> {

	@Override
	public ChangeStateCode convert(final Integer arg0) {
		final int id = arg0.intValue();
		switch (id) {
		case 0:
			return ChangeStateCode.PENDING;
		case 1:
			return ChangeStateCode.SUCCESS;
		case 2:
			return ChangeStateCode.ERROR;
		case 3:
			return ChangeStateCode.WARN;
		case 4:
			return ChangeStateCode.WAITING;
		case 5:
			return ChangeStateCode.DELIVERED;
		}
		return null;
	}

}
