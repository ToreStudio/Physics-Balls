/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author pepe
 */
public class DbBallJpaController implements Serializable {

    public DbBallJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DbBall dbBall) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DbSpace idSpace = dbBall.getIdSpace();
            if (idSpace != null) {
                idSpace = em.getReference(idSpace.getClass(), idSpace.getId());
                dbBall.setIdSpace(idSpace);
            }
            em.persist(dbBall);
            if (idSpace != null) {
                idSpace.getDbBallCollection().add(dbBall);
                idSpace = em.merge(idSpace);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DbBall dbBall) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DbBall persistentDbBall = em.find(DbBall.class, dbBall.getId());
            DbSpace idSpaceOld = persistentDbBall.getIdSpace();
            DbSpace idSpaceNew = dbBall.getIdSpace();
            if (idSpaceNew != null) {
                idSpaceNew = em.getReference(idSpaceNew.getClass(), idSpaceNew.getId());
                dbBall.setIdSpace(idSpaceNew);
            }
            dbBall = em.merge(dbBall);
            if (idSpaceOld != null && !idSpaceOld.equals(idSpaceNew)) {
                idSpaceOld.getDbBallCollection().remove(dbBall);
                idSpaceOld = em.merge(idSpaceOld);
            }
            if (idSpaceNew != null && !idSpaceNew.equals(idSpaceOld)) {
                idSpaceNew.getDbBallCollection().add(dbBall);
                idSpaceNew = em.merge(idSpaceNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dbBall.getId();
                if (findDbBall(id) == null) {
                    throw new NonexistentEntityException("The dbBall with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DbBall dbBall;
            try {
                dbBall = em.getReference(DbBall.class, id);
                dbBall.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dbBall with id " + id + " no longer exists.", enfe);
            }
            DbSpace idSpace = dbBall.getIdSpace();
            if (idSpace != null) {
                idSpace.getDbBallCollection().remove(dbBall);
                idSpace = em.merge(idSpace);
            }
            em.remove(dbBall);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DbBall> findDbBallEntities() {
        return findDbBallEntities(true, -1, -1);
    }

    public List<DbBall> findDbBallEntities(int maxResults, int firstResult) {
        return findDbBallEntities(false, maxResults, firstResult);
    }

    private List<DbBall> findDbBallEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DbBall.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DbBall findDbBall(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DbBall.class, id);
        } finally {
            em.close();
        }
    }

    public int getDbBallCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DbBall> rt = cq.from(DbBall.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
