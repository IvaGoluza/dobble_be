CREATE TABLE IF NOT EXISTS card (
id SERIAL PRIMARY KEY,
symbols VARCHAR(255) UNIQUE NOT NULL);

-- Insert cards - every card has 8 symbols
INSERT INTO card (symbols) VALUES
('exclamation#car#key#daisy#anchor#lock#note#lips'),
('cheese#clown#water#man#light#key#apple#zebra'),
('eye#clown#cactus#note#turtle#net#snow#bottle'),
('igloo#fire#dolphin#spots#snowman#cheese#bottle#car'),
('apple#note#candle#clock#dog#target#fire#question'),
('dobble#glasses#zebra#car#turtle#question#scissors#hammer'),
('birdie#tree#chess#question#skull#key#bottle#spider'),

('clown#tree#lips#heart#candle#hammer#dinosaur#igloo'),
('target#snow#dinosaur#chess#light#bomb#car#yinyang'),
('moon#skull#dragon#kitty#cheese#dinosaur#note#glasses'),
('fire#kitty#ghost#bolt#hammer#key#ice#snow'),
('kitty#spots#spider#daisy#heart#turtle#light#clock'),
('snowman#dobble#tree#kitty#net#apple#bomb#lock'),
('dobble#snow#candle#anchor#clover#spots#skull#man'),

('moon#dolphin#question#net#bolt#clover#lips#light'),
('daisy#target#ghost#skull#zebra#net#sun#igloo'),
('candle#light#leaf#bottle#ghost#lock#pencil#glasses'),
('water#ghost#chess#heart#note#snowman#scissors#clover'),
('dog#lock#igloo#chess#dragon#man#bolt#turtle'),
('zebra#dolphin#noentry#candle#kitty#chess#cactus#exclamation'),
('lips#ice#spots#apple#sun#eye#chess#glasses'),

('yinyang#ice#dolphin#clock#skull#scissors#lock#clown'),
('cactus#tree#sun#scissors#anchor#light#fire#dragon'),
('man#kitty#lips#scissors#target#bottle#carrot#ladybug'),
('net#candle#ice#ladybug#dragon#car#spider#water'),
('clock#moon#man#eye#ghost#car#tree#noentry'),
('yinyang#man#exclamation#glasses#birdie#fire#net#heart'),
('igloo#ladybug#bomb#clock#clover#glasses#cactus#key'),

('key#dragon#heart#leaf#dolphin#dobble#target#eye'),
('cactus#apple#bolt#car#pencil#carrot#heart#skull'),
('ice#anchor#moon#heart#bomb#dog#zebra#bottle'),
('pencil#noentry#key#dog#net#scissors#spots#dinosaur'),
('snowman#clock#pencil#dragon#zebra#lips#snow#birdie'),
('target#birdie#cactus#water#lock#hammer#moon#spots'),
('spider#clover#eye#zebra#fire#lock#dinosaur#carrot'),

('bolt#scissors#daisy#cheese#eye#birdie#candle#bomb'),
('clock#bottle#dinosaur#bolt#exclamation#water#dobble#sun'),
('yinyang#carrot#moon#snowman#key#sun#candle#turtle'),
('snow#carrot#dolphin#glasses#daisy#dog#water#tree'),
('noentry#note#light#igloo#ice#carrot#dobble#birdie'),
('spider#snowman#clown#target#glasses#noentry#anchor#bolt'),
('kitty#eye#yinyang#question#water#pencil#igloo#anchor'),

('chess#ladybug#dobble#clown#moon#fire#daisy#pencil'),
('birdie#apple#ghost#dinosaur#ladybug#anchor#dolphin#turtle'),
('dog#sun#leaf#clover#kitty#clown#car#birdie'),
('apple#noentry#bottle#dragon#yinyang#hammer#clover#daisy'),
('net#clock#anchor#leaf#carrot#hammer#cheese#chess'),
('bomb#dolphin#man#pencil#sun#note#spider#hammer'),
('heart#noentry#sun#lock#ladybug#cheese#question#snow'),

('carrot#spots#question#exclamation#clown#bomb#dragon#ghost'),
('clover#cheese#exclamation#pencil#target#tree#ice#turtle'),
('cheese#cactus#dog#dobble#ghost#lips#yinyang#spider'),
('scissors#spider#exclamation#apple#snow#igloo#moon#leaf'),
('bomb#turtle#noentry#fire#leaf#skull#water#lips'),
('tree#zebra#note#bolt#ladybug#leaf#yinyang#spots')
ON CONFLICT DO NOTHING;