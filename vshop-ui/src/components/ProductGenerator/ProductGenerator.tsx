import React, {useState} from 'react';
import './ProductGenerator.css'

interface Props {
}

const ProductGenerator: React.FC<Props> = () => {
    const url = 'http://localhost:8080/products/generate';
    const numberOfProducts = 100;

    const [inProgress, setInProgress] = useState(false);
    const [generatingError, setGeneratingError] = useState(false);

    const generateProducts = async (): Promise<void> => {
        setGeneratingError(false);
        setInProgress(true);
        console.log(`Hitting ${url}`);

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({numberOfProducts: numberOfProducts}),
            });

            if (response.ok) {
                setGeneratingError(false);
                console.log('Products generated successfully');
            } else {
                setGeneratingError(true)
                console.error('Failed to generate products');
            }
        } catch (error) {
            setGeneratingError(true)
            console.error('Error generating products', error);
        } finally {
            setInProgress(false);
        }
    };

    return (
        <div>
            <input type="button" value="Generate 100 more products" onClick={generateProducts}/>
            {inProgress && <span className="generating-message">Generating...</span>}
            {generatingError && <span className="generating-error">Cannot generate products...</span>}
        </div>
    );
};

export default ProductGenerator;
