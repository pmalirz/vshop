import React, { useEffect, useState } from 'react';
import './ProductBrowser.css';

interface Product {
    id?: string;
    name?: string;
    description?: string;
    price?: number;
    quantity?: number;
}

interface Props {
    searchString?: string;
    searchMinLength: number;
}

const ProductBrowser: React.FC<Props> = ({ searchString, searchMinLength }) => {
    const url = `http://localhost:8080/products?textContains=${searchString}`;

    const [products, setProducts] = useState<Product[]>([]);
    const [fetchError, setFetchError] = useState(false);

    useEffect(() => {
        let isMounted = true;

        const fetchData = async () => {
            try {
                if (!searchString || searchString.length < searchMinLength) {
                    setProducts([]);
                } else {
                    console.log(`Hitting ${url}`);
                    const response = await fetch(url);
                    const data = await response.json();
                    if (isMounted) {
                        setProducts(data.items);
                    }
                }
                setFetchError(false);
            } catch (error) {
                console.error(error);
                if (isMounted) {
                    setProducts([]);
                    setFetchError(true);
                }
            }
        };

        fetchData();

        return () => {
            isMounted = false;
        };
    }, [searchString, searchMinLength]);

    const renderProducts = (): JSX.Element | null => {
        if (products.length === 0) {
            return null;
        }

        return (
            <table className="product-table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Quantity</th>
                </tr>
                </thead>
                <tbody>
                {products.map((product) => (
                    <tr key={product.id}>
                        <td>{product.name}</td>
                        <td>{product.description}</td>
                        <td>{product.price}</td>
                        <td>{product.quantity}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };

    return (
        <div>
            <div className="found-products-count">Found {products.length} products</div>
            {fetchError && <div className="fetch-error">Cannot fetch products...</div>}
            {renderProducts()}
        </div>
    );
};

export default ProductBrowser;
