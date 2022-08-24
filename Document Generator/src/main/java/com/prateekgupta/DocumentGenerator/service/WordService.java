package com.prateekgupta.DocumentGenerator.service;

import java.io.ByteArrayInputStream;

public interface WordService {
    ByteArrayInputStream createDocument();
    byte[] createReport();
}
