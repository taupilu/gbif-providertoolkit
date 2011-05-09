/*
 * Copyright 2009 GBIF. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.gbif.ipt.utils;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * TODO: Documentation.
 */
public class XmlFileUtils {
  public static Reader getUtf8Reader(File file) throws FileNotFoundException {
    Reader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return reader;
  }

  public static Writer startNewUtf8File(File file) throws IOException {
    Writer writer = null;
    FileUtils.touch(file);
    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF8"));
    return writer;
  }

  public static Writer startNewUtf8XmlFile(File file) throws IOException {
    Writer writer = startNewUtf8File(file);
    writer.write("<?xml version='1.0' encoding='utf-8'?>\n");
    return writer;
  }
}