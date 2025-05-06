CREATE USER 'utilisateur'@'localhost' IDENTIFIED BY 'utilisateur123';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`utilisateur` TO `utilisateur`@`localhost`;
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`amis` TO `utilisateur`@`localhost`;
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`groupe_messages_prives` TO 'utilisateur'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`message_privee` TO 'utilisateur'@'localhost';
GRANT INSERT ON `conversa`.`signalement` TO 'utilisateur'@'localhost';

CREATE USER 'superadmin'@'localhost' IDENTIFIED BY 'superadmin123';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`utilisateur` TO 'superadmin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`amis` TO 'superadmin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`groupe_messages_prives` TO 'superadmin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`message_privee` TO 'superadmin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`signalement` TO 'superadmin'@'localhost';
FLUSH PRIVILEGES;