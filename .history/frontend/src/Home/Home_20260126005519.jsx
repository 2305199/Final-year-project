import styles from './Home.module.css'
function Home() {
    return ( <div>
    <h1>Welcome to the Home Page</h1>
   
    <div className={styles.Box}>
        <div className={styles.card} ><p>Last Scanned:</p></div>
        <div className={styles.card}><p>Phishing URLs found:</p></div>
        <button>Scan now</button>
    </div>
    </div>)
}
export default Home