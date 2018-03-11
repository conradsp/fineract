/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fineract.infrastructure.paymentgateway.gateway.util;

import org.joda.time.DateTime;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aomeri on 1/5/2016.
 */
public class DateUtil {

    public final static String DEFAULT_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";
    public final static String SHORT_DATE_FORMAT ="yyyy-MM-dd";

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDate(String strDate, String format)  {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date=null;
        try {
            date= sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int checkAge(DateTime dateOfBirth) {
        return Years.yearsBetween(dateOfBirth, new DateTime()).getYears();
    }
}