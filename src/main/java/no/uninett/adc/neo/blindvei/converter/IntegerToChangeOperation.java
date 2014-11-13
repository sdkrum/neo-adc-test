package no.uninett.adc.neo.blindvei.converter;

import no.uninett.adc.neo.domain.ChangeOperation;

import org.springframework.core.convert.converter.Converter;

public class IntegerToChangeOperation implements Converter<Integer, ChangeOperation> {

	@Override
	public ChangeOperation convert(final Integer arg0) {
		if (0 == arg0.intValue()) {
			return ChangeOperation.CREATE;
		}
		if (1 == arg0.intValue()) {
			return ChangeOperation.DELETE;
		}
		if (2 == arg0.intValue()) {
			return ChangeOperation.UPDATE;
		}
		return null;
	}

}
