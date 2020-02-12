/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.samples.porter.file.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *  Simple file storage implementation.
 *  Caution: file check and other security constraints not implemented. 
 */
@Component
public class LocalFileStoreService implements FileStoreService {
    // maxmum BUFFER_SIZE * BUFFER_NUM
    private static final int BUFFER_SIZE = 10240;

    private static final File BASE_FILE = new File(".");

    @Override
    public String uploadFile(MultipartFile file) {
        byte[] buffer = new byte[BUFFER_SIZE];
        String fileId = UUID.randomUUID().toString();

        File outFile = new File(BASE_FILE, fileId);
        int len;
        try (InputStream is = file.getInputStream(); OutputStream os = new FileOutputStream(outFile)) {
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
            return null;
        }
        return fileId;
    }

    @Override
    public boolean deleteFile(String id) {
        File outFile = new File(BASE_FILE, id);
        return outFile.delete();
    }

}
