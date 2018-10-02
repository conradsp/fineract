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

CREATE TABLE IF NOT EXISTS `payment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `client_id` BIGINT(20) NOT NULL,
  `account_id` BIGINT(20) NOT NULL,
  `payment_entity` TINYINT(1) NOT NULL,
  `payment_source_account` VARCHAR(100) NOT NULL,
  `payment_destination_account` VARCHAR(100) NOT NULL,
  `transaction_amount` DECIMAL(19,6) NOT NULL DEFAULT '0.000000',
  `payment_status` TINYINT(1) NOT NULL,
  `payment_direction` TINYINT(1) NOT NULL,
  `channel_ref_id` VARCHAR(150) DEFAULT NULL,
  `external_ref_id` VARCHAR(150) DEFAULT NULL,
  `channel_response_message` VARCHAR(250) DEFAULT null,
  `payment_channel_id` VARCHAR(150) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_client_id_for_payment` FOREIGN KEY (`client_id`) REFERENCES `m_client` (`id`),
  CONSTRAINT `fk_payment_created_by_m_appuser` FOREIGN KEY (`user_id`) REFERENCES `m_appuser` (`id`)
)ENGINE = InnoDB;