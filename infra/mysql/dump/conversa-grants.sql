CREATE USER 'utilisateur'@'localhost' IDENTIFIED BY 'utilisateur123';
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`utilisateur` TO `utilisateur`@`localhost`;
GRANT SELECT, INSERT, UPDATE, DELETE ON `conversa`.`amis` TO `utilisateur`@`localhost`;