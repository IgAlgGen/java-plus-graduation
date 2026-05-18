package ru.practicum.ewm.stats.analyzer.kafka;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class AvroDeserializer {

    public <T extends SpecificRecordBase> T deserialize(byte[] payload, Schema schema) {
        try {
            DatumReader<T> reader = new SpecificDatumReader<>(schema);
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(payload, null);
            return reader.read(null, decoder);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to deserialize Avro record", exception);
        }
    }
}
