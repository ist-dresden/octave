/*
 * Copyright (c) 2013 IST GmbH Dresden, the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * IST GmbH Dresden
 * Eisenstuckstraße 10, 01069 Dresden, Germany
 * All rights reserved.
 *
 * Name: octave.js
 * Autor: Mirko Zeibig
 * Datum: 07.03.2013 15:289:39
 */

$(document).ready(function() { 
    //$("#plugin_table").tablesorter(); 
    $('.detailButton').click(
            function(event) {
                bid = event.currentTarget.id;
                tnr = bid.substr(bid.lastIndexOf('-') + 1);
                did = '#test-detail-' + tnr;
                $(did).toggle();
                $(event.currentTarget).toggleClass('ui-icon-triangle-1-e');
                $(event.currentTarget).toggleClass('ui-icon-triangle-1-s');
            });
    }
); 
