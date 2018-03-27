Simple
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

It's a never-ending policy which just filters and processes files included in the corresponding URIs.

.. attention:: This policy is more oriented for testing purposes.
               It never stops and Kafka Connect is continuously trying to poll data from the FS(s).

Sleepy
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The behaviour of this policy is similar to Simple policy but on each execution it sleeps
and wait for the next one. Additionally, its custom properties allow to end it.

You can learn more about the properties of this policy :ref:`here<config_options-policies-sleepy>`.

Hdfs file watcher
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

It uses Hadoop notifications events and all create/append/close events will be reported
as new files to be ingested.

Just use it when you have HDFS URIs.

.. attention:: The URIs included in general property ``fs.uris`` will be filtered and only those
               ones which start with the prefix ``hdfs://`` will be watched. Also, this policy
               will only work for Hadoop versions 2.6.0 or higher.
