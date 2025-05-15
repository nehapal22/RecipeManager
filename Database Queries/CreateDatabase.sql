USE recipe;

CREATE TABLE IF NOT EXISTS recipes (
    recipeid INT AUTO_INCREMENT PRIMARY KEY,
    recipename VARCHAR(45) NOT NULL UNIQUE,
    instructions TEXT,
    cookingtime DOUBLE(5,2),
    CONSTRAINT unique_recipename UNIQUE (recipename)
);

CREATE TABLE IF NOT EXISTS ingredients (
    ingredientid INT AUTO_INCREMENT PRIMARY KEY,
    ingredientname VARCHAR(45) NOT NULL UNIQUE,
    quantity DOUBLE(5,2),
    quantitytype VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS recipes_ingredients (
    recipeid INT,
    ingredientid INT,
    FOREIGN KEY (recipeid) REFERENCES recipes(recipeid),
    FOREIGN KEY (ingredientid) REFERENCES ingredients(ingredientid),
    PRIMARY KEY (recipeid, ingredientid)
);

CREATE TABLE IF NOT EXISTS favorites (
    recipeid INT PRIMARY KEY,
    favoritedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipeid) REFERENCES recipes(recipeid)
);
