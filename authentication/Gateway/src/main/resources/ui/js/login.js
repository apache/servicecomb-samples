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

function loginAction() {
     var username = document.getElementById("username").value;
     var password = document.getElementById("paasword").value;
     var formData = {};
     formData.userName = username;
     formData.password = password;

     $.ajax({
        type: 'POST',
        url: "/api/user-service/v1/user/login",
        data: formData,
        success: function (data) {
            console.log(data);
            setCookie("session-id", data.sessiondId, 1);
            window.location = "/ui/upload.html";
        },
        error: function(data) {
            console.log(data);
            var error = document.getElementById("error");
            error.textContent="Login failed";
            error.hidden=false;
        },
        async: true
    });
}

function setCookie(name,value,days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}