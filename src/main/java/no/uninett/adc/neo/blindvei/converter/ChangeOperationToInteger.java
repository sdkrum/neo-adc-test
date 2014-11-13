package no.uninett.adc.neo.blindvei.converter;

import no.uninett.adc.neo.domain.ChangeOperation;

import org.springframework.core.convert.converter.Converter;

public class ChangeOperationToInteger implements Converter<ChangeOperation, Integer> {

	@Override
	public Integer convert(final ChangeOperation arg0) {
		return Integer.valueOf(arg0.getCode());
	}

}
