/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 2000, 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jssun.util.calendar;

import java.util.TimeZone;

/**
 * Gregorian calendar implementation.
 *
 * @author Masayoshi Okutsu
 * @since 1.5
 */

public class Gregorian extends BaseCalendar {

    static class Date extends BaseCalendar.Date {
      protected Date() {
        super();
    }

    protected Date(TimeZone zone) {
        super(zone);
    }

        @Override
				public int getNormalizedYear() {
            return getYear();
        }

        @Override
				public void setNormalizedYear(int normalizedYear) {
            setYear(normalizedYear);
        }
    }

    Gregorian() {
    }

    @Override
		public String getName() {
        return "gregorian";
    }

    @Override
		public Date getCalendarDate() {
        return getCalendarDate(System.currentTimeMillis(), newCalendarDate());
    }

    @Override
		public Date getCalendarDate(long millis) {
        return getCalendarDate(millis, newCalendarDate());
    }

    @Override
		public Date getCalendarDate(long millis, CalendarDate date) {
        return (Date) super.getCalendarDate(millis, date);
    }

    @Override
		public Date getCalendarDate(long millis, TimeZone zone) {
        return getCalendarDate(millis, newCalendarDate(zone));
    }

    @Override
		public Date newCalendarDate() {
        return new Date();
    }

    @Override
		public Date newCalendarDate(TimeZone zone) {
        return new Date(zone);
    }
}
