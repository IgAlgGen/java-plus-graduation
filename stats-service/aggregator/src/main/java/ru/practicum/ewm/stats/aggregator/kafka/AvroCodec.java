package ru.practicum.ewm.stats.aggregator.kafka;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class AvroCodec {

    public byte[] serialize(SpecificRecordBase record) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(record.getSchema());
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            writer.write(record, encoder);
            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to serialize Avro record", exception);
        }
    }

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
