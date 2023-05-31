import React, { useState, useEffect } from 'react';
import ProductBrowser from '../ProductBrowser/ProductBrowser';
import './ProductSearch.css'

interface Props {}

const ProductSearch: React.FC<Props> = () => {
    const searchMinLength = 3;
    const debounceTimeMs = 200;

    const [searchString, setSearchString] = useState('');
    const [isSearchStringTooShort, setIsSearchStringTooShort] = useState(false);

    useEffect(() => {
        let debounce: NodeJS.Timeout;

        const handleSearch = () => {
            setIsSearchStringTooShort(searchString.length > 0 && searchString.length < searchMinLength);
        };

        if (searchString.length >= searchMinLength) {
            debounce = setTimeout(handleSearch, debounceTimeMs);
        } else {
            handleSearch();
        }

        return () => clearTimeout(debounce);
    }, [searchString, searchMinLength]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>): void => {
        setSearchString(event.target.value);
    };

    return (
        <div>
            <label htmlFor="productSearch">Find a product: </label>
            <input
                type="text"
                id="productSearch"
                name="productSearch"
                onChange={handleChange}
            />
            {isSearchStringTooShort && <div className="search-error">Too short search phrase</div>}
            <div>
                <ProductBrowser searchString={searchString} searchMinLength={searchMinLength} />
            </div>
        </div>
    );
};

export default ProductSearch;
