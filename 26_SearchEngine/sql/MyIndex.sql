CREATE TABLE IF NOT EXISTS search_engine.MyIndex (
    id INT PRIMARY KEY AUTO_INCREMENT,
    page_id INT NOT NULL,
    lemma_id INT NOT NULL,
    rank DOUBLE NOT NULL)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

--index — поисковый индекс
--● id INT NOT NULL AUTO_INCREMENT;
--● page_id INT NOT NULL — идентификатор страницы;
--● lemma_id INT NOT NULL — идентификатор леммы;
--● rank FLOAT NOT NULL — ранг леммы на этой страницы