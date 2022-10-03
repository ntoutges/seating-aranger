# seating-aranger
Puts people into "pods" of 4 people, and for each generation (new line) ensures that none have sat together before.

## Custom format: JDON
- Java Dumb Object Notation
- A simpler version of JSON
- Format:
  * <key>: <value> // <comment>
  * key: name of the value
  * value: self explanatory
- Unlike JSON, JDON supports comments!
- JDON does not support spaces in the key or value -- use underscores instead
  * the <key> "my key" will be interpreted as "mykey"
