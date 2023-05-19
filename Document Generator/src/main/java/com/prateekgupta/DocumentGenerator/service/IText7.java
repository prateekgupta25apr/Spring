package com.prateekgupta.DocumentGenerator.service;

import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface IText7 {
    ByteArrayInputStream createDocument() throws DocumentException, IOException;
}
