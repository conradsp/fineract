--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

CREATE TABLE IF NOT EXISTS `payment_channel` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `channel_name` VARCHAR(150) NOT NULL,
  `channel_broker_endpoint` VARCHAR(250) NOT NULL,
  `channel_type` smallint(5) DEFAULT NULL,
  `is_active` TINYINT(1) NULL,
  `phone_number_default_region` VARCHAR(10) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_channel_name` (`channel_name` ASC),
  CONSTRAINT `fk_payment_channel_created_by_m_appuser` FOREIGN KEY (`user_id`) REFERENCES `m_appuser` (`id`)
)ENGINE = InnoDB;