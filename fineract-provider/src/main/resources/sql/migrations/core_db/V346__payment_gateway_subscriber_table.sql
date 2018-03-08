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

CREATE TABLE IF NOT EXISTS `payment_gateway_subscriber` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `client_id` BIGINT(20) NOT NULL,
  `entity_id` BIGINT(20) NOT NULL,
  `payment_entity` smallint(5) DEFAULT NULL,
  `payment_ref` VARCHAR(150) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `payment_gateway_subscriber_created_by_m_appuser` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  CONSTRAINT `payment_gateway_subscriber_created_by_m_appuser` FOREIGN KEY (`user_id`) REFERENCES `m_appuser` (`id`)
)ENGINE = InnoDB;