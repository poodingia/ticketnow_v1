/** @type {{images: {remotePatterns: [{protocol: string, hostname: string, pathname: string},{protocol: string, hostname: string}]}}} */
const nextConfig = {
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: 'res.cloudinary.com',
                pathname: '**',
            },
            {
                protocol: 'https',
                hostname: 'localhost',
            }
        ],
    },
};

export default nextConfig;
