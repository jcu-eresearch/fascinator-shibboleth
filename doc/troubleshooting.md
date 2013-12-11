Encountered Issues
====

Symbols in Username
====

If the Shibboleth attribute you set in `username_attribute` contains sybbols ("@" for example) you may throw an exception like:

    Traceback (most recent call last):
      File "default/default/scripts/detail.py", line 43, in __activate__
      File "default/default/scripts/detail.py", line 211, in _DetailData__readMetadata
      File "default/default/scripts/detail.py", line 207, in _DetailData__loadSolrData
                    at org.apache.commons.io.IOUtils.copyLarge(IOUtils.java:1025)
                    at org.apache.commons.io.IOUtils.copy(IOUtils.java:999)
                    at com.googlecode.fascinator.indexer.SolrIndexer.search(SolrIndexer.java:436)
                    at Invocation$Indexer$search$142d604b15b.invokeDelegateMethod(Invocation$Indexer$search$142d604b15b.java)
                    at org.apache.tapestry5.ioc.internal.services.AbstractInvocation.proceed(AbstractInvocation.java:117)
                    at com.googlecode.fascinator.redbox.PortalModule$2.advise(PortalModule.java:141)
                    at org.apache.tapestry5.ioc.internal.services.AbstractInvocation.proceed(AbstractInvocation.java:121)
                    at $Indexer_142d603c74f.search($Indexer_142d603c74f.java)
                    at $Indexer_142d603c74a.search($Indexer_142d603c74a.java)
                    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
                    at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
                    at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
                    at java.lang.reflect.Method.invoke(Unknown Source)
     
    java.lang.NullPointerException: java.lang.NullPointerException
