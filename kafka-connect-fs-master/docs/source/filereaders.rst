Avro
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Files with `Avro <http://avro.apache.org/>`__ format can be read with this reader.

The Avro schema is not needed due to is read from the file. The message sent
to Kafka is created by transforming the record by means of
`Confluent avro-converter <https://github.com/confluentinc/schema-registry/tree/master/avro-converter>`__
API.

More information about properties of this file reader :ref:`here<config_options-filereaders-avro>`.

Parquet
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Reads files with `Parquet <https://parquet.apache.org/>`__ format.

The reader takes advantage of the Parquet-Avro API and uses the Parquet file
as if it were an Avro file, so the message sent to Kafka is built in the same
way as the Avro file reader does.

.. warning:: Seeking Parquet files is a heavy task because the reader has to
             iterate over all records. If the policy processes the same file
             over and over again and has to seek the file, the performance
             can be affected.

More information about properties of this file reader :ref:`here<config_options-filereaders-parquet>`.

SequenceFile
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

`Sequence files <https://wiki.apache.org/hadoop/SequenceFile>`__ are one kind of
the Hadoop file formats which are serialized in key/value pairs.

This reader can process this file format and build a Kafka message with the
key/value pair. These two values are named ``key`` and ``value`` in the message
by default but you can customize these field names.

More information about properties of this file reader :ref:`here<config_options-filereaders-sequencefile>`.

Text
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Reads plain text files.

Each line represents one record which will be in a field
named ``value`` in the message sent to Kafka by default but you can
customize these field names.

More information about properties of this file reader :ref:`here<config_options-filereaders-text>`.

Delimited text
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Text file reader using a custom token to distinguish different columns on each line.

It allows to distinguish a header in the files and set the name of their columns
in the message sent to Kafka. If there is no header, the value of each column will be in
the field named ``column_N`` (**N** represents the column index) in the message.
Also, the token delimiter for columns is configurable.

More information about properties of this file reader :ref:`here<config_options-filereaders-delimited>`.

Agnostic
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Actually, this reader is a wrapper of the readers listing above.

It tries to read any kind of file format using an internal reader based on the file extension,
applying the proper one (Parquet, Avro, SecuenceFile, Text or Delimited text). In case of no
extension has been matched, the Text file reader will be applied.

Default extensions for each format:
* Parquet: .parquet
* Avro: .avro
* SequenceFile: .seq
* Delimited text: .tsv, .csv
* Text: any other sort of file extension.

More information about properties of this file reader :ref:`here<config_options-filereaders-agnostic>`.
