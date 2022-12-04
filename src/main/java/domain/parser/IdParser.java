package domain.parser;

import exceptii.ParsingException;

public class IdParser extends Parser<Long> {
    /**
     * parseaza un string in id
     * @param string - stringul
     * @return Long - id-ul
     * @throws ParsingException - daca parsarea nu este posibila
     */
    @Override
    public Long parse(String[] string) throws ParsingException {
        return parseId(string[0]);
    }
}
