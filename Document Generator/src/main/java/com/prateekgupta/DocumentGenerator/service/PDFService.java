package com.prateekgupta.DocumentGenerator.service;

import java.io.ByteArrayInputStream;

public interface PDFService {
    ByteArrayInputStream createDocument();

    byte[] createReport();
}
